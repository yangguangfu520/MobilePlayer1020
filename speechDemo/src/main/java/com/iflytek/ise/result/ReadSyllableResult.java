/**
 * 
 */
package com.iflytek.ise.result;

import com.iflytek.ise.result.util.ResultFormatUtil;

/**
 * <p>Title: ReadSyllableResult</p>
 * <p>Description: 中文单字评测结果</p>
 * <p>Company: www.iflytek.com</p>
 * @author iflytek
 * @date 2015年1月12日 下午5:03:14
 */
public class ReadSyllableResult extends Result {
	
	public ReadSyllableResult() {
		language = "cn";
		category = "read_syllable";
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[总体结果]\n")
			.append("评测内容：" + content + "\n")
			.append("朗读时长：" + time_len + "\n")
			.append("总分：" + total_score + "\n\n")
			.append("[朗读详情]").append(ResultFormatUtil.formatDetails_CN(sentences));
		
		return buffer.toString();
	}
}
