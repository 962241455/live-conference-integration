package com.cmnt.dbpick.manager.server.mapper;

import com.cmnt.dbpick.common.utils.ForkJoinUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public interface BaseImManager {
    Logger log = LoggerFactory.getLogger(BaseImManager.class);

    default void asyncSend(Runnable task) {
        CompletableFuture.runAsync(task, ForkJoinUtils.defaultPool())
                .exceptionally(e -> {
                    TradeEx<Throwable> trade = BaseImManager::print;
                    trade.trade(e);
                    return null;
                });
    }

    static void print(Throwable ex) {
        log.error("IM消息发送异常: {}", ex.getMessage());
    }

    @FunctionalInterface
    interface TradeEx<Throwable> {
        void trade(Throwable ex);
    }
}
