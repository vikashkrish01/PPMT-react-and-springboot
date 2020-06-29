import axios from "axios";
import {
  GET_ERRORS,
  GET_PROJECTS,
  GET_PROJECT,
  DELETE_PROJECT,
} from "../actions/actionTypes";

export const createProject = (project, history) => {
  return async (dispatch) => {
    console.log(project);
    try {
      await axios.post(
        "http://localhost:8082/api/project/createProject",
        project
      );
      history.push("/dashboard");
      dispatch({
        type: GET_ERRORS,
        payload: {},
      });
    } catch (err) {
      dispatch({
        type: GET_ERRORS,
        payload: err.response.data,
      });
    }
  };
};

export const getProjects = () => async (dispatch) => {
  const res = await axios.get("http://localhost:8082/api/project/getProjects");
  dispatch({
    type: GET_PROJECTS,
    payload: res.data,
  });
};

export const getProject = (id, history) => async (dispatch) => {
  try {
    const res = await axios.get(
      `http://localhost:8082/api/project/getProjectById/${id}`
    );
    dispatch({
      type: GET_PROJECT,
      payload: res.data,
    });
  } catch (error) {
    history.push("/dashboard");
  }
};

export const deleteProject = (id) => async (dispatch) => {
  await axios.delete(`http://localhost:8082/api/project/deleteProject/${id}`);
  dispatch({
    type: DELETE_PROJECT,
    payload: id,
  });
};
