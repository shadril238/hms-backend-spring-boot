package com.shadril.doctorservice.generator;

import com.shadril.doctorservice.repository.DoctorRepository;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DoctorIdGenerator implements IdentifierGenerator, ApplicationContextAware {

    private static ApplicationContext context;

    private volatile AtomicInteger counter = null;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {
        if (counter == null) {
            synchronized (this) {
                if (counter == null) {
                    DoctorRepository doctorRepository = context
                            .getBean(DoctorRepository.class);
                    String maxDoctorId = doctorRepository
                            .findMaxDoctorId()
                            .orElse("11-00000");
                    int maxId = Integer.parseInt(maxDoctorId.substring(3));
                    counter = new AtomicInteger(maxId);
                }
            }
        }

        String uniqueId = String.format("%05d", counter.incrementAndGet());
        return "11-" + uniqueId;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        DoctorIdGenerator.context = applicationContext;
    }
}