package org.egzi.treebuilder;

import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Collections.unmodifiableCollection;

/**
 * Created by Егор on 14.09.2016.
 */
@RequiredArgsConstructor
@Getter
@Setter
public abstract class AbstractTreeNode<K, V> implements TreeNode<K, V> {

    private final K id;

    private final K parentId;

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private volatile V value;

    private volatile TreeNode<K, V> parent;

    private Collection<TreeNode<K, V>> children = new ArrayList<>();

    private AtomicBoolean processed = new AtomicBoolean(false);

    private CountDownLatch latch = new CountDownLatch(1);

    public AbstractTreeNode(K id, V value, K parentId) {
        this(id, parentId);
        this.value = value;
    }

    public synchronized Collection<TreeNode<K, V>> getChildren() {

        return unmodifiableCollection(children);
    }

    public synchronized void addChildNode(TreeNode<K, V> node) {

        children.add(node);
    }

    @Override
    public V get() {
        return value;
    }

    public void set(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractTreeNode<?, ?> that = (AbstractTreeNode<?, ?>) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(parentId, that.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId);
    }

    @Override
    public boolean isVisited() {
        return processed.get();
    }

    @Override
    @SneakyThrows
    public void awaitVisited(long timeout, TimeUnit unit) {
        if (timeout == 0) {
            latch.await();
            return;
        }
        if (!latch.await(timeout, unit)) {
            throw new TimeoutException("Node with id: " + id + "was't visited for" + timeout + " " + unit);
        }
    }

    @Override
    public void markVisited() {
        if (!processed.compareAndSet(false, true)) {
            throw new IllegalStateException("Node is already processed");
        }
        latch.countDown();
    }
}