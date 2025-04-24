package com.mpw.model.jdfx.dstar;

/**
 * 图上的节点
 */
public class DNode {
    /**
     * 用于标识该节点
     */
    private int index;

    /**
     * 节点的h值
     */
    private long h;

    /**
     * 节点的k值
     */
    private long k;

    /**
     * 最短路径的回调节点
     */
    private DNode b;

    /**
     * 节点的状态
     */
    private DGraph.STATE state;

    private Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public DNode(int index) {
        this(index, DGraph.STATE.NEW);
    }

    public DNode(int index, DGraph.STATE state) {
        this.index = index;
        this.state = state;
    }

    @Override
    public int hashCode() {
        return this.index;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof DNode) {
            DNode node = (DNode) obj;
            return node.index == this.index;
        }
        return false;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getH() {
        return h;
    }

    public void setH(long h) {
        this.h = h;
    }

    public long getK() {
        return k;
    }

    public void setK(long k) {
        this.k = k;
    }

    public DNode getB() {
        return b;
    }

    public void setB(DNode b) {
        this.b = b;
    }

    public DGraph.STATE getState() {
        return state;
    }

    public void setState(DGraph.STATE state) {
        this.state = state;
    }
}
