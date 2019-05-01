/*------------------------------------------------------------------------
   File        : QueueProcessor
   Purpose     : 
   Syntax      : 
   Description : 
   Author(s)   : chthomso@progress.com
   Created     : 
   Notes       : 
 ----------------------------------------------------------------------*/

block-level on error undo, throw.

using OpenEdge.Logging.ISupportLogging from propath.
using psc.tasks.ITaskProcessor from propath.

class psc.queue.QueueProcessor 
    implements ITaskProcessor,ISupportLogging
    :
    define public property Logger as OpenEdge.Logging.ILogWriter no-undo 
        get():
            if not valid-object(Logger) then
                assign Logger = OpenEdge.Logging.LoggerBuilder:GetLogger(this-object:GetClass()).
            return Logger.
        end get.
        set.

    /*------------------------------------------------------------------------------
     Purpose:
     Notes:
    ------------------------------------------------------------------------------*/
    constructor public QueueProcessor (  ):
        super ().
        
        logger:trace('New QueueProcessor instance created').
    end constructor.

    method public integer runAllScheduledTasks(  ):
        define variable retcount as integer no-undo.
        
        assign 
            retcount = random(1,5000).
        
        logger:Debug(subst('Processed all requests: &1',retcount)).
        
        return retcount.    
    end method.
end class.