package net.sourceforge.solexatools.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sourceforge.seqware.common.model.Experiment;
import net.sourceforge.seqware.common.model.File;
import net.sourceforge.seqware.common.model.IUS;
import net.sourceforge.seqware.common.model.Lane;
import net.sourceforge.seqware.common.model.Processing;
import net.sourceforge.seqware.common.model.Sample;
import net.sourceforge.seqware.common.model.Study;
import net.sourceforge.seqware.common.model.WorkflowRun;

public class BulkUtil {

    public static List<File> getFiles(HttpServletRequest request, String nameList) {
        List<File> list = null;
        HttpSession session = request.getSession(false);
        if (nameList.equals("unknow")) {
            list = (List<File>) session.getAttribute("bulkDownloadFiles");
            if (list == null) {
                System.out.println("Get Analysis Files");
                list = (List<File>) session.getAttribute("analysisBulkDownloadFiles");
            }
        } else {
            list = (List<File>) session.getAttribute(nameList);
        }
        if (list == null) {
            list = Collections.synchronizedList(new LinkedList<File>());
        }
        return list;
    }

    public static void updateSelectedIds(HttpServletRequest request, String nameSelected, boolean isSelect, List<String> nodeIds) {
        List<String> list = getCurrentSelectedNodes(request, nameSelected);
        for (String id : nodeIds) {
            if (isSelect) {
                list = addSelectedId(id, list);
            } else {
                list.remove(id);
            }
        }
        setCurrentSelectedNodes(request, nameSelected, list);
    }

    public static String getSelectedIds(HttpServletRequest request, String nameSelected, String[] ids, String[] statuses) {
        String selectedIds = "";
        List<String> list = getCurrentSelectedNodes(request, nameSelected);
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            Integer status = Integer.parseInt(statuses[i]);
            if (status == 0) {
                list.remove(id);
            }
            if (status == 1) {
                list = addSelectedId(id, list);
            }
        }
        setCurrentSelectedNodes(request, nameSelected, list);
        return selectedIds;
    }

    private static List<String> addSelectedId(String addId, List<String> list) {
        boolean isAdd = true;
        for (String id : list) {
            if (id.equals(addId)) {
                isAdd = false;
                break;
            }
        }
        if (isAdd) {
            list.add(addId);
        }
        return list;
    }

    private static List<String> getCurrentSelectedNodes(HttpServletRequest request, String nameSelected) {
        List<String> list = null;
        if (nameSelected.equals("launchSelectedNodes")) {
            list = LaunchWorkflowUtil.getCurrentSelectedNodes(request);
        } else {
            list = (List<String>) request.getSession(false).getAttribute(nameSelected);
        }
        if (list == null) {
            list = new LinkedList<String>();
        }
        return list;
    }

    private static void setCurrentSelectedNodes(HttpServletRequest request, String nameSelected, List<String> list) {
        if (nameSelected.equals("launchSelectedNodes")) {
            LaunchWorkflowUtil.setCurrentSelectedNodes(request, list);
        } else {
            request.getSession(false).setAttribute(nameSelected, list);
        }
    }

    private static void selectedNessIUSNode(SortedSet<IUS> iuss, List<String> selectedIds) {
        for (String selectedObjectId : selectedIds) {
            for (IUS ius : iuss) {
                String iusUIId = Constant.IUS_PREFIX + ius.getIusId();
                if (selectedObjectId.equals(iusUIId)) {
                    ius.setIsSelected(true);
                }
            }
        }
    }

    public static SortedSet<IUS> selectIUSNode(List<String> selectedIds, Sample sample) {
        SortedSet<IUS> iuss = sample.getIUS();
        if (selectedIds != null) {
            selectedNessIUSNode(iuss, selectedIds);
            SortedSet<Processing> processings = new TreeSet<Processing>(sample.getProcessings());
            selectedNessProcessingNode(processings, selectedIds);
            SortedSet<Sample> samples = new TreeSet<Sample>(sample.getChildren());
            selectedNessSampleNode(samples, selectedIds);
        }
        return iuss;
    }

    private static void selectedNessSampleNode(SortedSet<Sample> samples, List<String> selectedIds) {
        for (String selectedObjectId : selectedIds) {
            for (Sample sample : samples) {
                String experimentUIId = Constant.SAMPLE_PREFIX + sample.getSampleId();
                if (selectedObjectId.equals(experimentUIId)) {
                    sample.setIsSelected(true);
                }
            }
        }
    }

    public static SortedSet<Sample> selectSampleNode(List<String> selectedIds, Experiment experiment) {
        SortedSet<Sample> samples = experiment.getSamples();
        if (selectedIds != null) {
            SortedSet<Processing> processings = new TreeSet<Processing>(experiment.getProcessings());
            selectedNessSampleNode(samples, selectedIds);
            selectedNessProcessingNode(processings, selectedIds);
        }
        return samples;
    }

    private static void selectedNessExperimentNode(SortedSet<Experiment> experiments, List<String> selectedIds) {
        for (String selectedObjectId : selectedIds) {
            for (Experiment experiment : experiments) {
                String experimentUIId = Constant.EXPERIMENT_PREFIX + experiment.getExperimentId();
                if (selectedObjectId.equals(experimentUIId)) {
                    experiment.setIsSelected(true);
                }
            }
        }
    }

    public static SortedSet<Experiment> selectExperimentNode(List<String> selectedIds, Study study) {
        SortedSet<Experiment> experiments = study.getExperiments();
        SortedSet<Processing> processings = new TreeSet<Processing>(study.getProcessings());
        if (selectedIds != null) {
            selectedNessExperimentNode(experiments, selectedIds);
            selectedNessProcessingNode(processings, selectedIds);
        }
        return experiments;
    }

    public static List<Study> selectStudyNode(List<String> selectedIds, List<Study> studies) {
        if (selectedIds != null) {
            for (String selectedObjectId : selectedIds) {
                for (Study study : studies) {
                    String studyUIId = Constant.STUDY_PREFIX + study.getStudyId();
                    if (selectedObjectId.equals(studyUIId)) {
                        study.setIsSelected(true);
                    }
                }
            }
        }
        return studies;
    }

    public static List<WorkflowRun> selectWorkflowRunNode(List<String> selectedIds, List<WorkflowRun> workflowRuns) {
        if (selectedIds != null) for (String selectedObjectId : selectedIds) {
            for (WorkflowRun workflowRun : workflowRuns) {
                String workflowRunUIId = Constant.WORKFLOW_RUN_PREFIX + workflowRun.getWorkflowRunId();
                if (selectedObjectId.equals(workflowRunUIId)) {
                    workflowRun.setIsSelected(true);
                }
            }
        }
        return workflowRuns;
    }

    private static void selectedNessProcessingNode(SortedSet<Processing> processings, List<String> selectedIds) {
        for (String selectedObjectId : selectedIds) {
            for (Processing processing : processings) {
                String processingUIId = Constant.PROCESSING_PREFIX + processing.getProcessingId();
                if (selectedObjectId.equals(processingUIId)) {
                    processing.setIsSelected(true);
                }
            }
        }
    }

    public static Set<Processing> selectProcessingNode(List<String> selectedIds, WorkflowRun workflowRun) {
        SortedSet<Processing> processings = workflowRun.getProcessings();
        if (selectedIds != null) {
            selectedNessProcessingNode(processings, selectedIds);
        }
        return processings;
    }

    private static void selectedNessProcessingNode(Set<File> files, Set<Processing> processings, List<String> selectedIds) {
        for (String selectedObjectId : selectedIds) {
            for (File file : files) {
                String fileUIId = Constant.FILE_PREFIX + file.getFileId();
                System.out.println("fileUI=" + fileUIId);
                if (selectedObjectId.equals(fileUIId)) {
                    file.setIsSelected(true);
                }
            }
        }
        List<WorkflowRun> workflowRuns = new ArrayList<WorkflowRun>();
        for (String selectedObjectId : selectedIds) {
            for (Processing processing : processings) {
                String processingUIId = Constant.PROCESSING_PREFIX + processing.getProcessingId();
                if (selectedObjectId.equals(processingUIId)) {
                    processing.setIsSelected(true);
                    if (processing.getWorkflowRun() != null) {
                        processing.getWorkflowRun().setIsSelected(true);
                    }
                }
                workflowRuns.add(processing.getWorkflowRun());
            }
        }
    }

    public static Set<Processing> selectProcessingNode(List<String> selectedIds, Processing proc) {
        if (selectedIds == null) return proc.getChildren();
        Set<File> files = proc.getFiles();
        Set<Processing> processings = proc.getChildren();
        selectedNessProcessingNode(files, processings, selectedIds);
        return processings;
    }

    public static Set<Processing> selectProcessingNode(List<String> selectedIds, Lane lane) {
        if (selectedIds == null) return lane.getProcessings();
        Set<File> files = new TreeSet<File>();
        Set<Processing> processings = lane.getProcessings();
        selectedNessProcessingNode(files, processings, selectedIds);
        return processings;
    }

    public static Set<Processing> selectProcessingNode(List<String> selectedIds, IUS ius) {
        if (selectedIds == null) return ius.getProcessings();
        Set<File> files = new TreeSet<File>();
        Set<Processing> processings = ius.getProcessings();
        selectedNessProcessingNode(files, processings, selectedIds);
        return processings;
    }
}
