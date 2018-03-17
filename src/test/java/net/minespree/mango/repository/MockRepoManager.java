package net.minespree.mango.repository;

import com.google.common.collect.Maps;
import lombok.Getter;
import net.minespree.mango.repository.base.AbstractRepoManager;

import java.util.Map;

/**
 * @since 09/02/2018
 */
public class MockRepoManager extends AbstractRepoManager<MockElement> {
    @Getter
    private final RepoCallbackManager<MockElement> callbackManager;
    private final Map<MockElement, Object> elements;

    public MockRepoManager(RepoCallbackManager<MockElement> callbackManager) {
        this.callbackManager = callbackManager;
        this.elements = Maps.newHashMap();
    }

    @Override
    public Object getRawValue(MockElement element) {
        return elements.get(element);
    }

    @Override
    protected void setRawValue(MockElement element, Object value) {
        elements.put(element, value);
    }

    @Override
    public Map<MockElement, Object> getEntries() {
        return elements;
    }
}
