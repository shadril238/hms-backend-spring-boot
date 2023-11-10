package com.shadril.patientservice.generator;

import com.shadril.patientservice.repository.PatientRepository;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PatientIdGenerator implements IdentifierGenerator, ApplicationContextAware {

    private static ApplicationContext context;

    private AtomicInteger counter = null;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        if (counter == null) {
            synchronized (this) {
                if (counter == null) {
                    PatientRepository patientRepository = context.getBean(PatientRepository.class);
                    String maxPatientId = patientRepository.findMaxPatientId()
                            .orElse("00-00000");
                    int maxId = Integer.parseInt(maxPatientId.substring(3));
                    counter = new AtomicInteger(maxId);
                }
            }
        }

        String uniqueId = String.format("%05d", counter.incrementAndGet());
        return "00-" + uniqueId;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        PatientIdGenerator.context = applicationContext;
    }
}
