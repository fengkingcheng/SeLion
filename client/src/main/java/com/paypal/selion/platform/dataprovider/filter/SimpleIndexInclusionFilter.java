/*-------------------------------------------------------------------------------------------------------------------*\
|  Copyright (C) 2015 eBay Software Foundation                                                                        |
|                                                                                                                     |
|  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance     |
|  with the License.                                                                                                  |
|                                                                                                                     |
|  You may obtain a copy of the License at                                                                            |
|                                                                                                                     |
|       http://www.apache.org/licenses/LICENSE-2.0                                                                    |
|                                                                                                                     |
|  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed   |
|  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for  |
|  the specific language governing permissions and limitations under the License.                                     |
\*-------------------------------------------------------------------------------------------------------------------*/

package com.paypal.selion.platform.dataprovider.filter;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import com.paypal.selion.logger.SeLionLogger;
import com.paypal.selion.platform.dataprovider.DataProviderException;
import com.paypal.selion.platform.dataprovider.DataProviderHelper;
import com.paypal.test.utilities.logging.SimpleLogger;

/**
 * This filter implementation filters data based on the indexes set. Indexes can be in the format of
 * <ul>
 * <li>
 * "1, 2, 3" for individual indexes.</li>
 * <li>"1-4, 6-8, 9-10" for ranges of indexes.</li>
 * <li>"1, 3, 5-7, 10, 12-14" for mixing individual and range of indexes.</li>
 * </ul>
 * This filter always collects data whose data index is in given filterIndexes.
 */
public class SimpleIndexInclusionFilter implements DataProviderFilter {

    protected static final SimpleLogger logger = SeLionLogger.getLogger();
    private int invocationCount = 0;
    private String filterIndexes;

    /**
     * @param filterIndexes - sets the indexes string that will be used in the filtering. It is in the format of:
     *
     * <ul>
     * <li>"1, 2, 3" for individual indexes.</li>
     * <li>"1-4, 6-8, 9-10" for ranges of indexes.</li>
     * <li>"1, 3, 5-7, 10, 12-14" for mixing individual and range of indexes.</li>
     * </ul>
     *
     */
    public SimpleIndexInclusionFilter(String filterIndexes) {
        checkArgument(filterIndexes != null, "Please provide valid indexes for filtering");
        this.filterIndexes = filterIndexes;
    }

    /**
     * This function identifies whether the object falls in the filtering criteria or not based on the indexes provided.
     * For this we are using the invocation count for comparing the index.
     *
     * @param data
     *            the object to be filtered.
     * @return boolean - true if object falls in the filter criteria.
     * @throws DataProviderException
     */
    @Override
    public boolean filter(Object data) throws DataProviderException {

        logger.entering(new Object[] { data });
        List<Integer> indexes = DataProviderHelper.parseIndexString(filterIndexes);
        int tempIndex = 0;
        try {
            invocationCount++;
            for (int index : indexes ) {
                if (invocationCount == index) {
                    logger.exiting(false);
                    return true;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataProviderException("Row with key '" + tempIndex + "' is not found");
        }
        logger.exiting(false);
        return false;
    }

    public String toString() {
        return "Filter Indexes :" + filterIndexes;
    }

}