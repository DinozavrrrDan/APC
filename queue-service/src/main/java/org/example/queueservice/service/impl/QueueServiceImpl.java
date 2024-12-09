package org.example.queueservice.service.impl;


import org.example.queueservice.model.PatientRequest;
import org.example.queueservice.service.Buffer;
import org.example.queueservice.service.QueueService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
public class QueueServiceImpl implements QueueService {
    private Map<Integer, Buffer<PatientRequest>> buffers = new ConcurrentHashMap<>();
    private static final Random RANDOM = new Random();
    private final CopyOnWriteArrayList<PatientRequest> statistic;
    private int bufferNumber = 3;
    private int buffersCapacity = 3;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicBoolean stepModeEnabled = new AtomicBoolean(false);
    private final RestTemplate restTemplate;

    public QueueServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.statistic = new CopyOnWriteArrayList<>();
        IntStream.range(0, bufferNumber).forEach(index -> buffers.put(index, new CircularBuffer<>(buffersCapacity)));
    }

    @Override
    public void setBufferNumber(int bufferNumber) {
        this.bufferNumber = bufferNumber;
        System.out.println("setBufferNumber: " + bufferNumber);
    }

    @Override
    public int getCounter() {
        return counter.get();
    }

    @Override
    public void setBufferCapacity(int bufferCapacity) {
        this.buffersCapacity = bufferCapacity;
        System.out.println("setBufferCapacity: " + buffersCapacity);
    }
    @Override
    public void regenerateBuffers() {
        buffers = new ConcurrentHashMap<>();
        IntStream.range(0, bufferNumber).forEach(index -> buffers.put(index, new CircularBuffer<>(buffersCapacity)));
    }

    @Override
    public synchronized void addPatientToQueue(PatientRequest patientRequest) {
        counter.incrementAndGet();
        System.out.println(patientRequest);
        int bufferIndex = RANDOM.nextInt(buffers.size());
        Buffer<PatientRequest> buffer = buffers.get(bufferIndex);
        patientRequest.setBufferId((long) bufferIndex);
        if (buffer.getSize() == buffer.getCapacity()) {
            PatientRequest patientToRemove = buffer.remove();
            patientToRemove.setWaitTime(System.currentTimeMillis() - patientToRemove.getRequestTimeInBuffer().getTime());
            statistic.add(patientToRemove);
            if (stepModeEnabled.get()) {
                sendStepLogToStatisticRemove(patientToRemove);
            }
        }
        patientRequest.setRequestTimeInBuffer(new Date());
        buffer.add(patientRequest);
        if(stepModeEnabled.get()) {
            sendStepLogToStatistic(patientRequest);
        }
    }

    @Override
    public synchronized PatientRequest getNextPatient() {
        int bufferIndex = RANDOM.nextInt(buffers.size());
        Buffer<PatientRequest> buffer = buffers.get(bufferIndex);
        PatientRequest patientToRemove = buffer.remove();
        if (patientToRemove != null) {
            patientToRemove.setWaitTime(System.currentTimeMillis() - patientToRemove.getRequestTimeInBuffer().getTime());
            System.out.println("getNextPatient: " + patientToRemove);
            if(stepModeEnabled.get()) {
                sendStepLogToStatisticRemove(patientToRemove);
            }
        }
        return patientToRemove;
    }

    @Override
    public CopyOnWriteArrayList<PatientRequest> getStatistic() {
        return statistic;
    }

    private void sendStepLogToStatistic(PatientRequest patientRequest){
        String statisticUrl =  "http://localhost:8089/statistic/step";
        String body = new Date() + " : Add patient to buffer: " + patientRequest.toString()
                + "\n Buffers: " +  buffersToString();
        restTemplate.postForObject(statisticUrl,
                body, String.class);
    }

    private void sendStepLogToStatisticRemove(PatientRequest patientRequest){
        //Тут состояние буфферов отправлять
        String statisticUrl =  "http://localhost:8089/statistic/step";
        String body = new Date() + " : Remove patient form buffer: " + patientRequest.toString()
                + "\n Buffers: " + buffersToString();
        restTemplate.postForObject(statisticUrl,
                body, String.class);
    }

    @Override
    public Map<Integer, Buffer<PatientRequest>> getBufferStatistic() {
        return buffers;
    }

    @Override
    public void setStepMode(boolean stepMode) {
        stepModeEnabled.set(stepMode);
    }

    private String buffersToString() {
        StringBuilder sb = new StringBuilder();
        for (Buffer<PatientRequest> buffer : buffers.values()) {
            sb.append(buffer.toString());
            sb.append("  ");
        }
        return sb.toString();
    }
}
