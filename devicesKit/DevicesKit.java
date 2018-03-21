package cornerstone.devicesKit;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Administrator on 2018/3/2.
 */

public class DevicesKit {
        /**
          * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
          * @return 平板返回 True，手机返回 False
          */
        public final boolean isPad( Context context) {
            Resources resources = context.getResources();
            return (resources.getConfiguration().screenLayout & 15) >= 3;
        }
}
