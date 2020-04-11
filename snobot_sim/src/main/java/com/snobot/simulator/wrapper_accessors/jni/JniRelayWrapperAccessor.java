
package com.snobot.simulator.wrapper_accessors.jni;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.snobot.simulator.jni.module_wrapper.RelayWrapperJni;
import com.snobot.simulator.module_wrapper.interfaces.IRelayWrapper;
import com.snobot.simulator.wrapper_accessors.RelayWrapperAccessor;

public class JniRelayWrapperAccessor implements RelayWrapperAccessor
{
    private static class RelayWrapper implements IRelayWrapper
    {
        private final int mHandle;

        private RelayWrapper(int aHandle)
        {
            mHandle = aHandle;
        }

        @Override
        public boolean isInitialized()
        {
            return RelayWrapperJni.isInitialized(mHandle);
        }

        @Override
        public void setInitialized(boolean aInitialized)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public String getName()
        {
            return RelayWrapperJni.getName(mHandle);
        }

        @Override
        public void setName(String aName)
        {
            RelayWrapperJni.setName(mHandle, aName);
        }

        @Override
        public boolean getWantsHidden()
        {
            return RelayWrapperJni.getWantsHidden(mHandle);
        }

        @Override
        public void setWantsHidden(boolean aVisible)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void close() throws Exception
        {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean getRelayReverse()
        {
            return RelayWrapperJni.getReverseValue(mHandle);
        }

        @Override
        public boolean getRelayForwards()
        {
            return RelayWrapperJni.getFowardValue(mHandle);
        }

        @Override
        public void setRelayReverse(boolean aReverse)
        {

        }

        @Override
        public void setRelayForwards(boolean aForwards)
        {

        }
    }

    private final Map<Integer, RelayWrapper> mWrappers = new HashMap<>();

    @Override
    public List<Integer> getPortList()
    {
        return IntStream.of(RelayWrapperJni.getPortList()).boxed().collect(Collectors.toList());
    }

    @Override
    public String getType(int aPort)
    {
        return null;
    }

    @Override
    public IRelayWrapper createSimulator(int aPort, String aType)
    {
        RelayWrapperJni.createSimulator(aPort, aType);
        mWrappers.put(aPort, new RelayWrapper(aPort));
        return mWrappers.get(aPort);
    }

    @Override
    public IRelayWrapper getWrapper(int aHandle)
    {
        if (!mWrappers.containsKey(aHandle))
        {
            mWrappers.put(aHandle, new RelayWrapper(aHandle));
        }
        return mWrappers.get(aHandle);
    }
}
