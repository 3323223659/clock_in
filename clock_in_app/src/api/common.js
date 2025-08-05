import { get } from '@/utils/request';

export const getQrCode = (className,sessionDate,time) => {
  return get('/web/clock_in/v1/common/getQrCode',{ className,sessionDate,time });
};
