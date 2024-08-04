package board.review;

import dbutil.BaseDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewDAO extends BaseDAO {
  private Connection con;

  public ReviewDAO() {
    try {
      con = getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Optional<List<ReviewResponseDTO>> selectReviewsByBoardPk(long boardPk) {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<ReviewResponseDTO> list = new ArrayList<>();
    try {
      pstmt = con.prepareStatement(ReviewSQL.SELECT_REVIEW_BY_BOARDPK);
      pstmt.setLong(1, boardPk);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        list.add(ReviewResponseDTO.builder()
                .reviewSeq(rs.getLong("review_seq"))
                .memberSeq(rs.getInt("member_seq"))
                .boardSeq(rs.getInt("board_seq"))
                .bookSeq(rs.getLong("booK_seq"))
                .nickname(rs.getString("nickname"))
                .title(rs.getString("title"))
                .pros(rs.getString("pros"))
                .cons(rs.getString("cons"))
                .features(rs.getString("features"))
                .wishes(rs.getString("wishes"))
                .grade(rs.getString("grade"))
                .rate(rs.getInt("rate"))
                .cdate(rs.getString("cdate"))
                .build());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeAll(rs, pstmt);
    }
    return Optional.ofNullable(list);
  }

  public int countByBoardPk(long boardPk) {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    int result = 0;
    try {
      pstmt = con.prepareStatement(ReviewSQL.COUNT);
      pstmt.setLong(1, boardPk);
      rs = pstmt.executeQuery();
      if (rs.next()) result = rs.getInt(1);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeAll(rs, pstmt);
    }
    return result;
  }

  public double selectAllRateByBoardPk(long boardPk) {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    double result = 0.0;
    try {
      pstmt = con.prepareStatement(ReviewSQL.SELECT_ALL_RATE_BY_BOARDPK);
      pstmt.setLong(1, boardPk);
      rs = pstmt.executeQuery();

      int cnt = 0;
      if (rs.next()) {
        result = rs.getDouble(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeAll(rs, pstmt);
    }

    return result;
  }

  public Optional<List<ReviewResponseDTO>> selectReviewsByBoardPkWithPaging(long boardPk, RequestDTO dto) {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<ReviewResponseDTO> list = new ArrayList<>();
    try {
      pstmt = con.prepareStatement(ReviewSQL.SELECT_REVIEW_BY_BOARDPK_WITH_PAGING);
      pstmt.setLong(1, boardPk);
      pstmt.setInt(2, dto.getStartNum());
      rs = pstmt.executeQuery();
      while (rs.next()) {
        list.add(ReviewResponseDTO.builder()
                .reviewSeq(rs.getLong("review_seq"))
                .memberSeq(rs.getInt("member_seq"))
                .boardSeq(rs.getInt("board_seq"))
                .bookSeq(rs.getLong("booK_seq"))
                .nickname(rs.getString("nickname"))
                .title(rs.getString("title"))
                .pros(rs.getString("pros"))
                .cons(rs.getString("cons"))
                .features(rs.getString("features"))
                .wishes(rs.getString("wishes"))
                .grade(rs.getString("grade"))
                .rate(rs.getInt("rate"))
                .cdate(rs.getString("cdate"))
                .build());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeAll(rs, pstmt);
    }
    return Optional.ofNullable(list);
  }

  public int insertReview(ReviewRequestDTO dto, long bookSeq) {
    PreparedStatement pstmt = null;
    int result = 0;
    try {
      pstmt = con.prepareStatement(ReviewSQL.INSERT);
      pstmt.setInt(1, dto.getRate());
      pstmt.setString(2, dto.getTitle());
      pstmt.setString(3, dto.getPros());
      pstmt.setString(4, dto.getCons());
      pstmt.setString(5, dto.getFeatures());
      pstmt.setString(6, dto.getWishes());
      pstmt.setInt(7, 0);
      pstmt.setLong(8, bookSeq);
      pstmt.setLong(9, dto.getMemberSeq());

      result = pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeAll(null, pstmt);
    }
    return result;
  }

  public Optional<ReviewResponseDTO> selectReviewByReviewPk(long reviewPk) {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    ReviewResponseDTO dto = null;
    try {
      pstmt = con.prepareStatement(ReviewSQL.SELECT_REVIEW_BY_REVIEWPK);
      pstmt.setLong(1, reviewPk);

      rs = pstmt.executeQuery();
      while (rs.next()) {
        dto = ReviewResponseDTO.builder()
                .reviewSeq(rs.getLong("review_seq"))
                .memberSeq(rs.getInt("member_seq"))
                .boardSeq(rs.getInt("board_seq"))
                .bookSeq(rs.getLong("booK_seq"))
                .nickname(rs.getString("nickname"))
                .title(rs.getString("title"))
                .pros(rs.getString("pros"))
                .cons(rs.getString("cons"))
                .features(rs.getString("features"))
                .wishes(rs.getString("wishes"))
                .grade(rs.getString("grade"))
                .rate(rs.getInt("rate"))
                .cdate(rs.getString("cdate"))
                .build();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return Optional.ofNullable(dto);
  }

  public int updateReviewByReviewPk(ReviewRequestDTO dto, long reviewPk) {
    PreparedStatement pstmt = null;
    int result = 0;
    try {
      pstmt = con.prepareStatement(ReviewSQL.UPDATE_REVIEW_BY_REVIEWPK);
      pstmt.setString(1, dto.getTitle());
      pstmt.setString(2, dto.getPros());
      pstmt.setString(3, dto.getCons());
      pstmt.setString(4, dto.getFeatures());
      pstmt.setString(5, dto.getWishes());
      pstmt.setInt(6, dto.getRate());
      pstmt.setLong(7, reviewPk);

      result = pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public int deleteReviewByReviewPk(long reviewPk) {
    PreparedStatement pstmt = null;
    int result = 0;

    try {
      pstmt = con.prepareStatement(ReviewSQL.DELETE_REVIEW_BY_REVIEWPK);
      pstmt.setLong(1, reviewPk);

      result = pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public int selectReviewPkByMemberPk(long bookSeq, long bookUser) {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    int result = 0;
    String sql = "select review_seq from review where book_seq = ? and member_seq = ?";

    try {
      pstmt = con.prepareStatement(sql);
      pstmt.setLong(1, bookSeq);
      pstmt.setLong(2, bookUser);

      rs = pstmt.executeQuery();
      if (rs.next()) result = rs.getInt("review_seq");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeAll(rs, pstmt);
    }
    return result;
  }
}
