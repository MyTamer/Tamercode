package org.fao.waicent.kids.giews.dao.impl;

import java.util.HashMap;
import java.util.List;
import org.fao.waicent.kids.giews.dao.ProjectLayerDAO;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;

public class ProjectLayerDAOImpl extends SqlMapDaoTemplate implements ProjectLayerDAO {

    /**
     * This method was generated by Abator for iBATIS. This method corresponds to the database table giews_test.project_layer
     * @abatorgenerated  Thu Nov 02 17:39:40 CET 2006
     */
    public ProjectLayerDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    public List getProjectLayers(String proj_ID) {
        return super.queryForList("project_layer.getProjectLayers", proj_ID);
    }

    public Object getFeatureId(HashMap map) {
        return super.queryForObject("project_layer.getFeatureId", map);
    }
}