package com.shadril.patientservice.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class PatientIdGenerator implements IdentifierGenerator {

    private final AtomicInteger counter = new AtomicInteger(0);
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        // Generate a unique 5-digit ID for the patient
        String uniqueId = String.format("%05d", counter.incrementAndGet());
        return "00-" + uniqueId;
    }
}
