import {LOGIN,LOGGING,LOGIN_FAIL,LOGIN_SUCCESS} from '../../constants/constant'

const initialState = {
    userInfo,
    loading : false,
    error : false,
}

export const loginReducer = (state = initialState, {type, payload}) => {
    switch(type) {
        case LOGGING :{
            return {
                ...state,
                loading : true,
            }
        }
        case LOGIN_FAIL : {
            return {
                ...state,
                error : true,
            }
        }
        case LOGIN_SUCCESS : {
            return {
                ...state,
                userInfo : payload,
            }
        }
        default : {
            return state
            }
        }
    }
