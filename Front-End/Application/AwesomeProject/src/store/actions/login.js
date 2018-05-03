import {LOGIN,LOGGING,LOGIN_SUCCESS,LOGIN_FAIL} from '../../constants/constant'

export const login = (username, pass) => {
    return {
        type: LOGIN,
        payload: {
            username,
            pass
        }
    }
}
export const logging = () => {
    return {
        type: LOGGING,
    }
}
export const login_fail = (error) => {
    return {
        type: LOGIN_FAIL,
        payload : error,
    }
}
export const login_success = (userInfo) => {
    return {
        type: LOGIN_SUCCESS,
        payload: userInfo,
    }
}