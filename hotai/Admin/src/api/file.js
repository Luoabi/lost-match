import request from '@/utils/request'

// 单文件上传
export function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/file/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 多文件上传
export function uploadMultipleFiles(files) {
  const formData = new FormData()
  files.forEach(file => {
    formData.append('files', file)
  })
  
  return request({
    url: '/file/upload-multiple',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 删除文件
export function deleteFile(url) {
  return request({
    url: '/file/delete',
    method: 'delete',
    params: { url }
  })
}
