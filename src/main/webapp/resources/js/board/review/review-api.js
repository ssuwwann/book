export const SERVER_IP = 'http://192.168.0.3:8080';

export const getReviewsByBoardPk = async (boardPk) => {
  const response = await $.get(`${SERVER_IP}/reviews/all/${boardPk}`)
  return response;
}

export const getReviewsByBoardPkWithPaging = async (boardPk, paging) => {
  const response = await $.ajax(`${SERVER_IP}/reviews/all/${boardPk}`, {
    contentType: 'application/json',
    type       : 'POST',
    data       : JSON.stringify(paging),
  })
  return response;
}

export const getReviewByReviewPk = async (reviewPk) => {
  const response = await $.ajax(`${SERVER_IP}/reviews/get/${reviewPk}`)
  return response;
}

export const addReview = async (boardPk, object) => {
  const response = await $.ajax(`${SERVER_IP}/reviews/add/${boardPk}`, {
    type       : 'POST',
    data       : JSON.stringify(object),
    processData: false,
    contentType: 'application/json'
  })
  return response;
}

export const updateReviewByReviewPk = async (boardPk, reviewPk, object) => {
  const response = await $.ajax(`${SERVER_IP}/reviews/update?board=${boardPk}&review=${reviewPk}`, {
    type       : 'POST',
    data       : JSON.stringify(object),
    processData: false,
    contentType: 'application/json'
  });
  return response;
}
