

#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"
#include "SnobotSim/ModuleWrapper/Interfaces/IGyroWrapper.h"
#include "SnobotSim/ModuleWrapper/Interfaces/IAccelerometerWrapper.h"

#include <memory>

class CtrePigeonImuSim : public AModuleWrapper
{
public:

static constexpr int CTRE_OFFSET = 400;
    static const std::string TYPE;
    const std::string& GetType() override
    { 
        return TYPE; 
    }

    CtrePigeonImuSim(int abasePort);

    
    void SetInitialized(bool aIsInitialized) override;

    std::shared_ptr<IGyroWrapper> getYawWrapper();
    std::shared_ptr<IGyroWrapper> getPitchWrapper();
    std::shared_ptr<IGyroWrapper> getRollWrapper();
    
    std::shared_ptr<IAccelerometerWrapper> getXWrapper();
    std::shared_ptr<IAccelerometerWrapper> getYWrapper();
    std::shared_ptr<IAccelerometerWrapper> getZWrapper();

protected:
    std::shared_ptr<IGyroWrapper> mYaw;
    std::shared_ptr<IGyroWrapper> mPitch;
    std::shared_ptr<IGyroWrapper> mRoll;
    
    std::shared_ptr<IAccelerometerWrapper> mX;
    std::shared_ptr<IAccelerometerWrapper> mY;
    std::shared_ptr<IAccelerometerWrapper> mZ;
};