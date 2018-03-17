package net.minespree.mango.scheduler.base;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.RunnableScheduledFuture;

/**
 * @since 14/02/2018
 */
public interface ListenableScheduledFuture<T> extends RunnableScheduledFuture<T>, ListenableFuture<T> {
}
