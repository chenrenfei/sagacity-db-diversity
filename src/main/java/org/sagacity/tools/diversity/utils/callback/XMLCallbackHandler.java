/**
 * 
 */
package org.sagacity.tools.diversity.utils.callback;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @project sagacity-core
 * @description xml编辑反调抽象类
 * @author chenrf $<a href="mailto:zhongxuchen@hotmail.com">联系作者</a>$
 * @version $id:XMLCallbackHandler.java,Revision:v1.0,Date:2009-12-30 上午12:34:46
 *          $
 */
public abstract class XMLCallbackHandler {
	/**
	 * 处理xml document对象
	 * 
	 * @param doc
	 * @param root
	 * @return
	 */
	public abstract Object process(Document doc, Element root) throws Exception;
}
