package com.proptiger.oracle.model.master;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ReasonHelper {

    private static final List<Integer> PRESALES_VERIFIED_COMMENTS = Arrays.asList(new Integer[] {
            34,
            1299,
            1303,
            1342,
            1339                                                 });
    private static final List<Integer> CONFCALL_VERIFIED_COMMENTS = Arrays.asList(new Integer[] { 1303 });
    private static final List<Integer> MEETING_DONE_COMMENTS      = Arrays.asList(new Integer[] { 22 });
    private static final List<Integer> SV_DONE_COMMENTS           = Arrays.asList(new Integer[] { 23 });

    public static boolean isPresalesVerified(Collection<Integer> reasons) {
        for (Integer reason : reasons) {
            if (isPresalesVerified(reason)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isConfCallVerified(Collection<Integer> reasons) {
        for (Integer reason : reasons) {
            if (isConfCallVerified(reason)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMeetingDone(Collection<Integer> reasons) {
        for (Integer reason : reasons) {
            if (isMeetingDone(reason)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSiteVisitDone(Collection<Integer> reasons) {
        for (Integer reason : reasons) {
            if (isSiteVisitDone(reason)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPresalesVerified(Integer reason) {
        return PRESALES_VERIFIED_COMMENTS.contains(reason);
    }

    public static boolean isConfCallVerified(Integer reason) {
        return CONFCALL_VERIFIED_COMMENTS.contains(reason);
    }

    public static boolean isMeetingDone(Integer reason) {
        return MEETING_DONE_COMMENTS.contains(reason);
    }

    public static boolean isSiteVisitDone(Integer reason) {
        return SV_DONE_COMMENTS.contains(reason);
    }

}
