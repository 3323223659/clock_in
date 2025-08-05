import { post } from "@/utils/request";

export const checkIn = (userId, sessionIds, location, flushTime) => {
  return post("/web/clock_in/v1/sessionAndCheckIn/checkIn", {
    userId,
    sessionIds,
    location,
    flushTime,
  });
};

export const changeStatus = (
  name,
  isPresent,
  courseLocation,
  courseSessionId
) => {
  return post("/web/clock_in/v1/sessionAndCheckIn/insertClockInAttendance", {
    name,
    isPresent,
    courseLocation,
    courseSessionId,
  });
};
