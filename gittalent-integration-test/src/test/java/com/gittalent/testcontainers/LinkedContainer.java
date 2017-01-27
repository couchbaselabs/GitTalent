package com.gittalent.testcontainers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.traits.LinkableContainer;

/**
 * Created by ldoguin on 1/10/17.
 */
public class LinkedContainer<SELF extends LinkedContainer<SELF>> extends GenericContainer<SELF> {

    public LinkedContainer(String name) {
        super(name);
    }

    public SELF withLinkToContainer(LinkableContainer otherContainer, String alias) {
        addLink(otherContainer, alias);
        return self();
    }
}
