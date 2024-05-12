package org.example.guice;

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.example.database.DatabaseAccess;
import org.example.database.impl.DatabaseAccessImpl;
import org.example.job.ExpiredReservationsDeactivation;
import org.example.rest.PublicController;
import org.example.service.BookService;
import org.example.service.LockService;
import org.example.service.OtpService;
import org.example.service.RentalService;
import org.example.service.impl.BookServiceImpl;
import org.example.service.impl.OtpServiceMock;
import org.example.service.impl.PrimitiveLockService;
import org.example.service.impl.RentalServiceImpl;
import org.hibernate.SessionFactory;

import javax.persistence.Persistence;

public class Module implements com.google.inject.Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(ExpiredReservationsDeactivation.class).asEagerSingleton();
        binder.bind(PublicController.class);
        binder.bind(DatabaseAccess.class).to(DatabaseAccessImpl.class);
        binder.bind(RentalService.class).to(RentalServiceImpl.class);
        binder.bind(LockService.class).to(PrimitiveLockService.class);
        binder.bind(OtpService.class).to(OtpServiceMock.class);
    }

    @Provides
    @Singleton
    public SessionFactory sessionFactory() {
        return Persistence.createEntityManagerFactory("prod")
                .unwrap(SessionFactory.class);
    }

    @Provides
    @Singleton
    public BookService bookService(DatabaseAccess databaseAccess) {
        return new BookServiceImpl(databaseAccess);
    }
}
