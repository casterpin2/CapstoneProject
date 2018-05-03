import { fork, all } from 'redux-saga/effects';
import login from './login';
import account from './account';
// import notification from './notification';

const rootSaga = function* () {
  yield all([
    ...login.map(watcher => fork(watcher)),
  ]);
};
export default rootSaga;
