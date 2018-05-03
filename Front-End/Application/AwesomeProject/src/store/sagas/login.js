import {
    LOGIN,
    LOGIN_FAIL,
    LOGIN_SUCCESS,
    LOGGING
  } from "../../constants/constant"
  import login from "../../api/login"
  import {
      login,
      logging,
      login_success,
      login_fail,
  } from '../actions/login'
  import firebase from 'react-native-firebase'

  const requestLogin = function*({ payload }) {
    console.log("request login:, ", payload);
    try {
      const user = yield firebase
        .auth()
        .signInAndRetrieveDataWithUsernameAndPassword(
          payload.username,
          payload.password
        );
      if (userInfor) {
        yield put(loginSuccess(user.userInfor));
      } else {
        yield put(authRequestFailed("login failed"));
      }
    } catch (error) {
      yield put(authRequestFailed(error));
    }
  };

  const fetchWatcher = function*() {
    yield all([
      takeLatest(LOGIN, requestLogin),
    ]);
  };
  
  // root saga reducer
  export default [fetchWatcher];
