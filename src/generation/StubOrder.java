package generation;

import generation.Factory;
import generation.MazeConfiguration;
import generation.MazeContainer;
import generation.MazeFactory;
import generation.Order;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class StubOrder implements Order {

    private MazeConfiguration mazeConfig;

    private Factory factory;

    private int skill;
    private Builder builder;
    private boolean isPerfect;

    public StubOrder() {
        super();
        setBuilder(Order.Builder.DFS); // default maze builder is DFS
        factory = new MazeFactory();
        mazeConfig = new MazeContainer();
        this.isPerfect = false;
        this.skill = 0;
    }

    public void setSkillLevel(int newSkill) {
        this.skill = newSkill;
    }

    @Override
    public int getSkillLevel() {
        return this.skill;
    }

    private void setBuilder(Builder builder) {
        this.builder = builder ;
    }

    @Override
    public Builder getBuilder() {
        return this.builder;
    }

    @Override
    public boolean isPerfect() {
        return isPerfect;
    }

    public void start() {
        factory.order(this);
        factory.waitTillDelivered();
    }

    public void start(Builder builder) {
        setBuilder(builder);
        factory.order(this);
        factory.waitTillDelivered();
    }

    @Override
    public void deliver(MazeConfiguration mazeConfig) {
        this.mazeConfig = mazeConfig;
    }

    public MazeConfiguration getMazeConfig() {
        return this.mazeConfig;
    }

    @Override
    public void updateProgress(int percentage) {

    }
}
