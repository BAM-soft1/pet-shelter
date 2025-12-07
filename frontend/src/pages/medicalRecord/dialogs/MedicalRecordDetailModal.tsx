import type { MedicalRecord } from "@/types/types";
import { Dialog, DialogContent, DialogFooter } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import {
  HeartIcon,
  BeakerIcon,
  CurrencyDollarIcon,
  CalendarDaysIcon,
  IdentificationIcon,
  SparklesIcon,
} from "@heroicons/react/24/outline";
import { HeartIcon as HeartSolid } from "@heroicons/react/24/solid";

type MedicalRecordDetailModalProps = {
  record: MedicalRecord | null;
  isOpen: boolean;
  onClose: () => void;
};

export default function MedicalRecordDetailModal({
  record,
  isOpen,
  onClose,
}: MedicalRecordDetailModalProps) {
  if (!record) return null;

  // Safely get species name
  const speciesName =
    typeof record.animal?.species === "object"
      ? record.animal?.species?.name
      : record.animal?.species;

  // Safely get breed name
  const breedName =
    typeof record.animal?.breed === "object"
      ? record.animal?.breed?.name
      : record.animal?.breed;

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-4xl max-h-[90vh] overflow-y-auto p-0 bg-gradient-to-br from-slate-900 via-blue-900 to-slate-900 border-0">
        {/* Animated background particles */}
        <div className="absolute inset-0 overflow-hidden pointer-events-none">
          <div className="absolute top-10 left-10 w-72 h-72 bg-purple-500 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-pulse" />
          <div className="absolute top-20 right-10 w-72 h-72 bg-pink-500 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-pulse delay-700" />
          <div className="absolute bottom-10 left-20 w-72 h-72 bg-blue-500 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-pulse delay-1000" />
        </div>

        <div className="relative z-10 p-8">
          {/* Header with glowing effect */}
          <div className="text-center mb-8">
            <div className="inline-flex items-center justify-center w-20 h-20 rounded-full bg-gradient-to-r from-red-500 via-red-500 to-blue-500 mb-4 shadow-lg shadow-purple-500/50 animate-pulse">
              <HeartSolid className="w-10 h-10 text-white" />
            </div>
            <h1 className="text-4xl font-bold bg-gradient-to-r from-pink-400 via-blue-400 to-blue-400 bg-clip-text text-transparent mb-2">
              Medical Record
            </h1>
            <div className="flex items-center justify-center gap-4 text-gray-400">
              <span className="flex items-center gap-1">
                <IdentificationIcon className="w-4 h-4" />
                #{record.id}
              </span>
              <span className="text-purple-500">•</span>
              <span className="flex items-center gap-1">
                <CalendarDaysIcon className="w-4 h-4" />
                {new Date(record.date).toLocaleDateString("da-DK", {
                  year: "numeric",
                  month: "long",
                  day: "numeric",
                })}
              </span>
            </div>
          </div>

          {/* Animal Card with Image */}
          <div className="mb-8 p-6 rounded-2xl bg-white/5 backdrop-blur-sm border border-white/10 hover:border-purple-500/50 transition-all duration-300 group">
            <div className="flex items-center gap-6">
              {/* Animal Image */}
              {record.animal?.imageUrl ? (
                <div className="relative">
                  <div className="w-24 h-24 rounded-2xl overflow-hidden shadow-lg shadow-emerald-500/30 group-hover:scale-105 transition-transform ring-2 ring-emerald-400/50">
                    <img
                      src={record.animal.imageUrl}
                      alt={record.animal?.name || "Animal"}
                      className="w-full h-full object-cover"
                    />
                  </div>
                  <div className="absolute -bottom-1 -right-1 w-6 h-6 bg-emerald-500 rounded-full flex items-center justify-center">
                    <SparklesIcon className="w-4 h-4 text-white" />
                  </div>
                </div>
              ) : (
                <div className="w-24 h-24 rounded-2xl bg-gradient-to-r from-emerald-400 to-cyan-400 flex items-center justify-center text-3xl font-bold text-white shadow-lg shadow-emerald-500/30 group-hover:scale-105 transition-transform">
                  {record.animal?.name?.charAt(0) || "?"}
                </div>
              )}

              {/* Animal Info */}
              <div className="flex-1">
                <p className="text-sm text-gray-400 uppercase tracking-wider mb-1">Patient</p>
                <h2 className="text-2xl font-bold text-white mb-2">
                  {record.animal?.name || "Unknown"}
                </h2>
                <div className="flex flex-wrap gap-2">
                  {speciesName && (
                    <span className="px-3 py-1 rounded-full bg-emerald-500/20 text-emerald-400 text-sm font-medium">
                      {speciesName}
                    </span>
                  )}
                  {breedName && (
                    <span className="px-3 py-1 rounded-full bg-cyan-500/20 text-cyan-400 text-sm font-medium">
                      {breedName}
                    </span>
                  )}
                  {record.animal?.sex && (
                    <span className="px-3 py-1 rounded-full bg-purple-500/20 text-purple-400 text-sm font-medium capitalize">
                      {record.animal.sex}
                    </span>
                  )}
                </div>
              </div>

              <SparklesIcon
                className="w-6 h-6 text-yellow-400 animate-spin"
                style={{ animationDuration: "3s" }}
              />
            </div>
          </div>

          {/* Info Cards Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
            {/* Diagnosis Card */}
            <div className="group p-6 rounded-2xl bg-gradient-to-br from-rose-500/10 to-pink-500/10 border border-rose-500/20 hover:border-rose-500/50 hover:shadow-lg hover:shadow-rose-500/20 transition-all duration-300">
              <div className="flex items-center gap-3 mb-4">
                <div className="p-3 rounded-xl bg-rose-500/20 group-hover:bg-rose-500/30 transition-colors">
                  <HeartIcon className="w-6 h-6 text-rose-400" />
                </div>
                <h3 className="text-lg font-semibold text-rose-400 uppercase tracking-wider">
                  Diagnosis
                </h3>
              </div>
              <p className="text-white text-lg leading-relaxed">{record.diagnosis}</p>
            </div>

            {/* Treatment Card */}
            <div className="group p-6 rounded-2xl bg-gradient-to-br from-blue-500/10 to-cyan-500/10 border border-blue-500/20 hover:border-blue-500/50 hover:shadow-lg hover:shadow-blue-500/20 transition-all duration-300">
              <div className="flex items-center gap-3 mb-4">
                <div className="p-3 rounded-xl bg-blue-500/20 group-hover:bg-blue-500/30 transition-colors">
                  <BeakerIcon className="w-6 h-6 text-blue-400" />
                </div>
                <h3 className="text-lg font-semibold text-blue-400 uppercase tracking-wider">
                  Treatment
                </h3>
              </div>
              <p className="text-white text-lg leading-relaxed">{record.treatment}</p>
            </div>
          </div>

          {/* Cost Card - Full Width */}
          <div className="group p-6 rounded-2xl bg-gradient-to-r from-emerald-500/10 via-green-500/10 to-teal-500/10 border border-emerald-500/20 hover:border-emerald-500/50 hover:shadow-lg hover:shadow-emerald-500/20 transition-all duration-300 mb-8">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="p-3 rounded-xl bg-emerald-500/20 group-hover:bg-emerald-500/30 transition-colors">
                  <CurrencyDollarIcon className="w-6 h-6 text-emerald-400" />
                </div>
                <h3 className="text-lg font-semibold text-emerald-400 uppercase tracking-wider">
                  Total Cost
                </h3>
              </div>
              <div className="text-right">
                <p className="text-4xl font-bold bg-gradient-to-r from-emerald-400 to-teal-400 bg-clip-text text-transparent">
                  ${record.cost.toFixed(2)}
                </p>
              </div>
            </div>
          </div>

          <DialogFooter>
            <Button
              onClick={onClose}
              className="w-full py-6 text-lg font-semibold bg-gradient-to-r from-blue-600 via-blue-600 to-indigo-600 hover:from-purple-500 hover:via-pink-500 hover:to-indigo-500 border-0 rounded-xl shadow-lg shadow-purple-500/30 hover:shadow-purple-500/50 transition-all duration-300 hover:scale-[1.02]"
            >
              Close Record
            </Button>
          </DialogFooter>
        </div>
      </DialogContent>
    </Dialog>
  );
}