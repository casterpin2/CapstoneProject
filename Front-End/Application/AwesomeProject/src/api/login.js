import requestServer from './requestServer'

export const login = async (username, password, deviceId) => {
    const url = ''
    // https://dev-sacado.wesave.vn/api/user/login
    const data = new FormData();
    const method = 'POST'

    // data.append('username', username)
    // data.append('password', password)
    // data.append('device_id', 'abcxyz')

    return requestServer(url, data, method)
}

