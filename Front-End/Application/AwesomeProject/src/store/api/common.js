import { create } from 'apisauce';
import configs from '../../constants/congifs';

const API = create({
  baseURL: configs.endPoint,
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json'
  }
});

export { API };
