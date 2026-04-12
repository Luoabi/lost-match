import request from '@/utils/request'

// 获取日志列表
export function getLogList(params) {
  return request({
    url: '/logs/list',
    method: 'get',
    params
  })
}

// 获取日志详情
export function getLogDetail(id) {
  return request({
    url: `/logs/detail/${id}`,
    method: 'get'
  })
}

// 清空日志
export function clearLogs(params) {
  return request({
    url: '/logs/clear',
    method: 'delete',
    params
  })
}

// 导出日志
export function exportLogs(params) {
  return request({
    url: '/logs/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
