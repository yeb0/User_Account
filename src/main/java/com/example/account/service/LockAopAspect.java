package com.example.account.service;

import com.example.account.aop.AccountLockIdInterface;
import com.example.account.dto.UseBalance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LockAopAspect {
    private final LockService lockService;

    //Aspect 문법 - 어떤 경우에 이 Aspect를 적용할 것인가 ?
    @Around("@annotation(com.example.account.aop.AccountLock) && args(request)")
    public Object aroundMethod(
            ProceedingJoinPoint pjp,
            AccountLockIdInterface request
    ) throws Throwable {
        // lock 취득 시도
        lockService.lock(request.getAccountNumber());
        try {
            return pjp.proceed();
        } finally {
            // lock 해제
            lockService.unlock(request.getAccountNumber());

        }
    }
}
