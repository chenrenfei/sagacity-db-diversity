package org.sagacity.tools.diversity.model;

import java.io.Serializable;

public class ProduceModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4042604600876880774L;

	/**
	 * 存储过程名称
	 */
	private String produceName;

	/**
	 * 存储过程类型
	 */
	private String productType;

	/**
	 * @return the produceName
	 */
	public String getProduceName() {
		return produceName;
	}

	/**
	 * @param produceName the produceName to set
	 */
	public void setProduceName(String produceName) {
		this.produceName = produceName;
	}

	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}

	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	
}
