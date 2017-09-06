package com.transformuk.hee.tis.reference.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@ApiModel(description = "A search response containing a limited number of results and a warning if the limit has been reached")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LimitedListResponse<T> implements Serializable{

  public static final String LIMIT_REACHED_CODE = "LIMIT_REACHED";
  private static final long serialVersionUID =1L;

  private int total;
  private String messageCode;
  private List<T> list;

  public LimitedListResponse() {
     }

  /**
   * Builds a response, populates the limit reached message if list size >= given limit
   * Please note this does not truncate the list if it's above the limit
   *
   * @param list  the list of entities - NOT NULL
   * @param limit the max limit > 0
   */
  public LimitedListResponse(List<T> list, int limit) {
    checkNotNull(list);
    checkArgument(limit > 0);
    this.setList(list);
    if (list.size() >= limit) {
      this.setMessageCode(LimitedListResponse.LIMIT_REACHED_CODE);
    }
    this.setTotal(list.size());
  }

  @ApiModelProperty(required = true, value = "The total number of results, this will be the limit if the maximum limit has been reached")
  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  @ApiModelProperty(required = true, value = "The message - warns if the result maximum limit has been reached")
  public String getMessageCode() {
    return messageCode;
  }

  public void setMessageCode(String messageCode) {
    this.messageCode = messageCode;
  }

  @ApiModelProperty(required = true, value = "the list of results")
  public List<T> getList() {
    return list;
  }

  public void setList(List<T> list) {
    this.list = list;
  }
}
