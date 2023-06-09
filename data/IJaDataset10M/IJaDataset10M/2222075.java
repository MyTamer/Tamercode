package egovframework.com.cop.ncm.service.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import egovframework.com.cop.ncm.service.NameCard;
import egovframework.com.cop.ncm.service.NameCardUser;
import egovframework.com.cop.ncm.service.NameCardVO;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

/**
 * 명함정보를 관리하기 위한 데이터 접근 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------       --------    ---------------------------
 *   2009.3.28  이삼섭          최초 생성
 *
 * </pre>
 */
@Repository("NcrdManageDAO")
public class NcrdManageDAO extends EgovComAbstractDAO {

    /**
     * 명함 정보를 삭제한다.
     * 
     * @param nameCard
     * @throws Exception
     */
    public void deleteNcrdItem(NameCard nameCard) throws Exception {
        update("NcrdManageDAO.deleteNcrdItem", nameCard);
    }

    /**
     * 명함 정보를 등록한다.
     * 
     * @param nameCard
     * @throws Exception
     */
    public void insertNcrdItem(NameCard nameCard) throws Exception {
        insert("NcrdManageDAO.insertNcrdItem", nameCard);
    }

    /**
     * 명함사용자 정보를 등록한다.
     * 
     * @param ncrdUser
     * @throws Exception
     */
    public void insertNcrdUseInf(NameCardUser ncrdUser) throws Exception {
        insert("NcrdManageDAO.insertNcrdUseInf", ncrdUser);
    }

    /**
     * 명함 정보에 대한 상세정보를 조회한다.
     * 
     * @param nameCard
     * @return
     * @throws Exception
     */
    public NameCardVO selectNcrdItem(NameCard nameCard) throws Exception {
        NameCardVO vo = new NameCardVO();
        vo = (NameCardVO) selectByPk("NcrdManageDAO.selectNcrdItem", nameCard);
        return vo;
    }

    /**
     * 명함 정보에 대한 목록을 조회한다.
     * 
     * @param nameCard
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<NameCardVO> selectNcrdItemList(NameCardVO ncrdVO) throws Exception {
        return list("NcrdManageDAO.selectNcrdItemList", ncrdVO);
    }

    /**
     * 
     * @param nameCard
     * @return
     * @throws Exception
     */
    public int selectNcrdItemListCnt(NameCardVO ncrdVO) throws Exception {
        return (Integer) getSqlMapClientTemplate().queryForObject("NcrdManageDAO.selectNcrdItemListCnt", ncrdVO);
    }

    /**
     * 명함 정보에 대한 목록 전체 건수를 조회한다.
     * 
     * @param ncrdUser
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<NameCardUser> selectNcrdUseInfs(NameCardUser ncrdUser) throws Exception {
        return list("NcrdManageDAO.selectNcrdUseInfs", ncrdUser);
    }

    /**
     * 명함사용자 정보에 대한 목록 전체 건수를 조회한다.
     * 
     * @param ncrdUser
     * @return
     * @throws Exception
     */
    public int selectNcrdUseInfsCnt(NameCardUser ncrdUser) throws Exception {
        return (Integer) getSqlMapClientTemplate().queryForObject("NcrdManageDAO.selectNcrdUseInfsCnt", ncrdUser);
    }

    /**
     * 명함 정보를 수정한다.
     * 
     * @param nameCard
     * @throws Exception
     */
    public void updateNcrdItem(NameCard nameCard) throws Exception {
        update("NcrdManageDAO.updateNcrdItem", nameCard);
    }

    /**
     * 명함사용자 정보를 수정한다.
     * 
     * @param ncrdUser
     * @throws Exception
     */
    public void updateNcrdUseInf(NameCardUser ncrdUser) throws Exception {
        update("NcrdManageDAO.updateNcrdUseInf", ncrdUser);
    }

    /**
     * 내 명함 정보에 대한 목록을 조회한다.
     * 
     * @param ncrdVO
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<NameCardVO> selectMyNcrdItemList(NameCardVO ncrdVO) throws Exception {
        return list("NcrdManageDAO.selectMyNcrdItemList", ncrdVO);
    }

    /**
     * 내 명함 정보에 대한 목록 전체 건수를 조회한다.
     * 
     * @param ncrdVO
     * @return
     * @throws Exception
     */
    public int selectMyNcrdItemListCnt(NameCardVO ncrdVO) throws Exception {
        return (Integer) getSqlMapClientTemplate().queryForObject("NcrdManageDAO.selectMyNcrdItemListCnt", ncrdVO);
    }
}
