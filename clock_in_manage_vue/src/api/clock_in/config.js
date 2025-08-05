import request from '@/utils/request'

// 查询配置信息列表
export function listConfig(query) {
  return request({
    url: '/clock_in/config/list',
    method: 'get',
    params: query
  })
}

// 查询配置信息详细
export function getConfig(id) {
  return request({
    url: '/clock_in/config/' + id,
    method: 'get'
  })
}

// 新增配置信息
export function addConfig(data) {
  return request({
    url: '/clock_in/config',
    method: 'post',
    data: data
  })
}

// 修改配置信息
export function updateConfig(data) {
  return request({
    url: '/clock_in/config',
    method: 'put',
    data: data
  })
}

// 删除配置信息
export function delConfig(id) {
  return request({
    url: '/clock_in/config/' + id,
    method: 'delete'
  })
}
