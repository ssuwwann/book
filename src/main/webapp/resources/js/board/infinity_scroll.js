import {getReviewByReviewPk, getReviewsByBoardPkWithPaging} from "./review/review-api.js";
import {drawContent, drwaReview} from "./draw.js";
import {closeModal, modal} from "./modal.js";

const paging = {
  startNum: 0,
  count   : 0
}
let boardPk = 0;

const io = new IntersectionObserver(entries => {
  let reviewsByBoardPkWithPaging = '';
  let obj = '';
  entries.forEach(async entry => {
    if (entry.intersectionRatio > 0) {
      reviewsByBoardPkWithPaging = await getReviewsByBoardPkWithPaging(boardPk, paging);
      obj = JSON.parse(reviewsByBoardPkWithPaging);

      $('.review-list').append(drwaReview(obj.data));
      paging.startNum += 5;
      paging.count = obj.count;
    }

    if (paging.count / paging.startNum < 1) {
      io.unobserve(entry.target)
    }

  })

  const modalCancleBtnEle = $('#modalCancle');
  $(document).on('click', 'li a', async (e) => {
    const target = $(e.currentTarget);
    const targetChildObj = target.find('div[class^="review-card-"]')[0];
    const targetClassName = targetChildObj.className;
    const reviewPk = targetClassName.substring(targetClassName.lastIndexOf("-") + 1);

    const data = await getReviewByReviewPk(reviewPk);
    const obj = JSON.parse(data);
    drawContent(obj);

    $(document).click(e => {
      if ($(e.target).attr('class')?.startsWith('modal-wrapper')) {
        closeModal(e)
      }
    });

    modalCancleBtnEle.click(closeModal);

  })
})

// 무한 스크롤 관찰 대상
export const viewTarget = (target, pk) => {
  $('.review-list').empty();
  boardPk = pk;
  io.observe(target[0]);
}