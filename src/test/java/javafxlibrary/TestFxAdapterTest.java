/*
 * Copyright 2017-2018   Eficode Oy
 * Copyright 2018-       Robot Framework Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javafxlibrary;

import javafxlibrary.utils.TestFxAdapter;
import mockit.Mocked;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testfx.api.FxRobotInterface;

import static junit.framework.TestCase.fail;

public abstract class TestFxAdapterTest {
    public FxRobotInterface getRobot() {
        return robot;
    }

    @Mocked
    private FxRobotInterface robot;

    @BeforeClass
    public static void setupTests() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        try {
            org.testfx.api.FxToolkit.registerPrimaryStage();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Initialization failed");
        }
    }

    @Before
    public void initJfxToolkit() {
        TestFxAdapter.setRobot(robot);
    }
}
