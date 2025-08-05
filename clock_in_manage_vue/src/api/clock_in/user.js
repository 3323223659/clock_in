import request from "@/utils/request";

// 查询学生列表
export function listUser(query) {
  return request({
    url: "/clock_in/user/list",
    method: "get",
    params: query,
  });
}

// 查询学生详细
export function getUser(id) {
  return request({
    url: "/clock_in/user/" + id,
    method: "get",
  });
}

// 新增学生
export function addUser(data) {
  return request({
    url: "/clock_in/user",
    method: "post",
    data: data,
  });
}

// 修改学生
export function updateUser(data) {
  return request({
    url: "/clock_in/user",
    method: "put",
    data: data,
  });
}

// 删除学生
export function delUser(id) {
  return request({
    url: "/clock_in/user/" + id,
    method: "delete",
  });
}

export function test() {
  return request({
    url: "/test",
    method: "get",
  });
}
