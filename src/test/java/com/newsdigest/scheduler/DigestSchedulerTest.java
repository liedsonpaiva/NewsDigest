package com.newsdigest.scheduler;

import com.newsdigest.service.DigestDispatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DigestSchedulerTest {

    @Mock
    private DigestDispatchService dispatchService;

    @InjectMocks
    private DigestScheduler digestScheduler;

    @Test
    void mustCallProcessPendingDigests() {

        // Act (Ação)
        digestScheduler.processDigests();

        //Assert (afirmação)
        verify(dispatchService, times(1)).processPendingDigests();
    }
}
