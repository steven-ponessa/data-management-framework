package com.ibm.wfm.beans;

import java.util.List;

public interface NaryTreeNodeInterface<T> {
	public static int PRE_ORDER = 1;
	public static int POST_ORDER = 2;
	public static int IN_ORDER = 4;
	public static int PRE_ORDER_ITERATE = 8;
	public static int POST_ORDER_ITERATE = 16;
	public static int IN_ORDER_ITERATE = 32;
	public List<T> getChildren();
}
