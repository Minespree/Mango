package net.minespree.mango.scheduler.base;

import com.google.common.util.concurrent.AbstractFuture;

import java.util.concurrent.RunnableFuture;

/**
 * Represents a runnable abstract listenable future task.
 *
 * @since 14/02/2018
 */
public abstract class RunnableAbstractFuture<T> extends AbstractFuture<T> implements RunnableFuture<T> {
}
