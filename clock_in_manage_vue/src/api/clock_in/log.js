import request from '@/utils/request'

// 查询打卡记录列表
export function listLog(query) {
  return request({
    url: '/clock_in/log/list',
    method: 'post',
    data: query
  })
}

// 查询打卡记录详细
export function getLog(id) {
  return request({
    url: '/clock_in/log/' + id,
    method: 'get'
  })
}

// 新增打卡记录
export function addLog(data) {
  return request({
    url: '/clock_in/log',
    method: 'post',
    data: data
  })
}

// 修改打卡记录
export function updateLog(data) {
  return request({
    url: '/clock_in/log',
    method: 'put',
    data: data
  })
}

// 删除打卡记录
export function delLog(id) {
  return request({
    url: '/clock_in/log/' + id,
    method: 'delete'
  })
}

export function getClassAndCourse(data) {
  return request({
    //url: '/clock_in/log/getClassAndCourse',
    url:'/clock_in/log/getClassroomsAndClasses',
    method: 'post',
    data:data
  })
}



