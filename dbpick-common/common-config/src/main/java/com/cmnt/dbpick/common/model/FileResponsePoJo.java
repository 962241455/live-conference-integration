package com.cmnt.dbpick.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class FileResponsePoJo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8160516981030868782L;

	private String fileUrl;

	private String objectKey;

	private Integer type;

	private String content;

	private String requestId;
}
