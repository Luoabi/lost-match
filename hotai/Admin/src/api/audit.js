import request from '@/utils/request'

// 获取审核列表
export function getAuditList(params) {
  return request({
    url: '/audit/list',
    method: 'get',
    params
  })
}

// 审核通过
export function approveAudit(data) {
  return request({
    url: '/audit/approve',
    method: 'post',
    data
  })
}

// 审核拒绝
export function rejectAudit(data) {
  return request({
    url: '/audit/reject',
    method: 'post',
    data
  })
}
