import { get } from '@/utils/request';
import { post } from '@/utils/request';
export const getClassLocationList = () => {
  return get('/web/clock_in/v1/sessionAndCheckIn/getAllClassLocation');
};

export const getStudentList = (sessionIds) => {
  return get('/web/clock_in/v1/sessionAndCheckIn/getClassroomAttendance', { sessionIds } );
};

export const changeStatus = (name, isPresent, courseLocation, sessionIds) => {
  return post('/web/clock_in/v1/sessionAndCheckIn/insertClockInAttendance', { name, isPresent, courseLocation, sessionIds } );
};