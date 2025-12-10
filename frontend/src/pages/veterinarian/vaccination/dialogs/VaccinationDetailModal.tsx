import type { Vaccination } from "@/types/types";
import { Dialog, DialogContent, DialogFooter } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import {
  BeakerIcon,
  CalendarDaysIcon,
  IdentificationIcon,
  SparklesIcon,
  ClockIcon,
  CheckCircleIcon,
} from "@heroicons/react/24/outline";

type VaccinationDetailModalProps = {
  vaccination: Vaccination | null;
  isOpen: boolean;
  onClose: () => void;
};

export default function VaccinationDetailModal({
  vaccination,
  isOpen,
  onClose,
}: VaccinationDetailModalProps) {
  if (!vaccination) return null;

  const typeName =
    typeof vaccination.vaccinationType === "object"
      ? vaccination.vaccinationType?.vaccineName
      : vaccination.vaccinationType;

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto p-0 bg-gradient-to-br from-slate-950 via-indigo-950 to-slate-950 border border-indigo-500/20 shadow-2xl">
        {/* Animated background grid */}
        <div className="absolute inset-0 overflow-hidden pointer-events-none opacity-30">
          <div className="absolute inset-0 bg-[linear-gradient(to_right,#4f46e510_1px,transparent_1px),linear-gradient(to_bottom,#4f46e510_1px,transparent_1px)] bg-[size:4rem_4rem]" />
        </div>

        {/* Glow effects */}
        <div className="absolute inset-0 overflow-hidden pointer-events-none">
          <div className="absolute -top-24 -left-24 w-96 h-96 bg-indigo-600 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-pulse" />
          <div className="absolute -bottom-24 -right-24 w-96 h-96 bg-purple-600 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-pulse" style={{ animationDelay: '1s' }} />
        </div>

        {/* Content */}
        <div className="relative z-10">
          {/* Header with gradient border */}
          <div className="text-center pt-8 pb-6 px-8 border-b border-indigo-500/20">
            <div className="inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-br from-indigo-500 to-purple-600 mb-4 shadow-lg shadow-indigo-500/50 relative">
              <div className="absolute inset-0 rounded-2xl bg-gradient-to-br from-indigo-400 to-purple-500 opacity-0 group-hover:opacity-100 transition-opacity blur-xl" />
              <BeakerIcon className="w-8 h-8 text-white relative z-10" />
            </div>
            
            <h1 className="text-3xl font-bold text-white mb-3">
              Vaccination Details
            </h1>
            
            <div className="flex items-center justify-center gap-4 text-sm">
              <span className="flex items-center gap-1.5 text-indigo-300">
                <IdentificationIcon className="w-4 h-4" />
                ID: {vaccination.id}
              </span>
              <span className="text-indigo-500/50">•</span>
              <span className="flex items-center gap-1.5 text-indigo-300">
                <CheckCircleIcon className="w-4 h-4" />
                Administered
              </span>
            </div>
          </div>

          {/* Main content */}
          <div className="p-8 space-y-6">
            {/* Date Card - Featured */}
            <div className="bg-gradient-to-br from-indigo-500/20 to-purple-500/20 rounded-xl p-6 border border-indigo-500/30 backdrop-blur-sm">
              <div className="flex items-center gap-3 mb-4">
                <div className="w-10 h-10 rounded-lg bg-indigo-500/30 flex items-center justify-center">
                  <CalendarDaysIcon className="w-5 h-5 text-indigo-300" />
                </div>
                <h2 className="text-lg font-semibold text-white">Timeline</h2>
              </div>
              
              <div className="grid grid-cols-2 gap-4">
                <div className="bg-slate-900/50 rounded-lg p-4 border border-indigo-500/10">
                  <p className="text-xs text-indigo-400 mb-1 uppercase tracking-wider">Date Administered</p>
                  <p className="text-white font-medium">
                    {new Date(vaccination.dateAdministered).toLocaleDateString("da-DK", {
                      year: "numeric",
                      month: "long",
                      day: "numeric",
                    })}
                  </p>
                </div>
                
                <div className="bg-slate-900/50 rounded-lg p-4 border border-purple-500/10">
                  <p className="text-xs text-purple-400 mb-1 uppercase tracking-wider flex items-center gap-1">
                    <ClockIcon className="w-3 h-3" />
                    Next Due Date
                  </p>
                  <p className="text-white font-medium">
                    {new Date(vaccination.nextDueDate).toLocaleDateString("da-DK", {
                      year: "numeric",
                      month: "long",
                      day: "numeric",
                    })}
                  </p>
                </div>
              </div>
            </div>

            {/* Animal Information Card */}
            <div className="bg-white/5 rounded-xl p-6 border border-white/10 backdrop-blur-sm hover:bg-white/10 transition-colors">
              <div className="flex items-center gap-3 mb-4">
                <div className="w-10 h-10 rounded-lg bg-yellow-500/20 flex items-center justify-center">
                  <SparklesIcon className="w-5 h-5 text-yellow-400" />
                </div>
                <h2 className="text-lg font-semibold text-white">Animal Information</h2>
              </div>
              
              <div className="space-y-3">
                <div className="flex justify-between items-center py-2 border-b border-white/5">
                  <span className="text-sm text-gray-400">Name</span>
                  <span className="text-white font-medium">{vaccination.animal?.name || "N/A"}</span>
                </div>
                <div className="flex justify-between items-center py-2">
                  <span className="text-sm text-gray-400">Species</span>
                  <span className="text-white font-medium">{vaccination.animal?.species?.name || "N/A"}</span>
                </div>
              </div>
            </div>

    
            <div className="bg-white/5 rounded-xl p-6 border border-white/10 backdrop-blur-sm hover:bg-white/10 transition-colors">
              <div className="flex items-center gap-3 mb-4">
                <div className="w-10 h-10 rounded-lg bg-green-500/20 flex items-center justify-center">
                  <BeakerIcon className="w-5 h-5 text-green-400" />
                </div>
                <h2 className="text-lg font-semibold text-white">Vaccine Details</h2>
              </div>
              
              <div className="space-y-3">
                <div className="flex justify-between items-center py-2">
                  <span className="text-sm text-gray-400">Vaccine Name</span>
                  <span className="text-white font-medium">{typeName || "N/A"}</span>
                </div>
              </div>
            </div>
          </div>

          {/* Footer */}
          <DialogFooter className="p-6 border-t border-indigo-500/20 bg-slate-950/50">
            <Button 
              variant="secondary" 
              onClick={onClose}
              className="bg-indigo-500/20 hover:bg-indigo-500/30 text-white border border-indigo-500/30 transition-all"
            >
              Close
            </Button>
          </DialogFooter>
        </div>
      </DialogContent>
    </Dialog>
  );
}