import { useEffect, useState } from "react";
import { DogFactsService } from "../../api/dogfacts";
import type { DogFact } from "../../types/types";
import { Button } from "../../components/ui/button";
import MainLayout from "../../components/layout/MainLayout";
import { PawPrint, RefreshCw } from "lucide-react";

const DogFacts = () => {
  const [fact, setFact] = useState<DogFact | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [isFlipped, setIsFlipped] = useState<boolean>(false);

  const fetchFact = async () => {
    if (loading) return;

    // Start by flipping back to front
    setIsFlipped(false);
    setLoading(true);

    await new Promise((resolve) => setTimeout(resolve, 600));

    try {
      const data = await DogFactsService.getDogFacts(1);
      if (data && data.length > 0) {
        setFact(data[0]);
      }
    } catch (err) {
      console.error(err);
      setFact("Could not fetch a dog fact right now. Woof!");
    } finally {
      setLoading(false);
      // Flip to reveal
      setIsFlipped(true);
    }
  };

  useEffect(() => {
    fetchFact();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <>
      <MainLayout>
        <div className="container mx-auto px-4 py-4 flex flex-col items-center relative pb-16">
          <div className="mb-14 text-center animate-in fade-in slide-in-from-top duration-700">
            <div className="inline-flex items-center gap-3 mb-4 bg-white/80 backdrop-blur-sm px-6 py-3 rounded-full shadow-md border border-blue-100">
              <PawPrint className="w-6 h-6 text-blue-600" />
              <span className="text-sm font-semibold text-blue-600 tracking-wide uppercase">Fun Facts</span>
            </div>
            <h1 className="text-6xl md:text-7xl font-extrabold leading-none bg-linear-to-b from-blue-600 via-indigo-600 to-purple-600 bg-clip-text text-transparent mb-4">
              Dog Facts
            </h1>
            <p className="text-lg text-slate-600 max-w-md mx-auto">Discover fascinating insights about our four-legged companions</p>
          </div>

          {/* Card Container with Perspective */}
          <div
            className="relative w-full max-w-lg h-[420px] group mb-12 animate-in fade-in slide-in-from-bottom duration-700 delay-200"
            style={{ perspective: "1500px" }}
          >
            {/* Flippable Inner Container */}
            <div
              className="relative w-full h-full transition-all duration-700 ease-out"
              style={{
                transformStyle: "preserve-3d",
                transform: isFlipped ? "rotateY(180deg)" : "rotateY(0deg)",
              }}
            >
              {/* Front Face */}
              <div
                className="absolute w-full h-full bg-linear-to-br from-blue-600 via-indigo-600 to-purple-600 text-white rounded-3xl shadow-2xl group-hover:shadow-blue-500/25 flex flex-col items-center justify-center transition-all duration-300"
                style={{ backfaceVisibility: "hidden" }}
              >
                <div className="relative mb-3">
                  <div className="absolute inset-0 bg-white/30 rounded-full blur-xl"></div>
                  <div className="relative bg-white/20 backdrop-blur-md p-6 rounded-full border-2 border-white/40">
                    <PawPrint className="w-16 h-16 text-white drop-shadow-lg" />
                  </div>
                </div>
                <h2 className="text-3xl font-bold text-white mb-2 drop-shadow-md">Did You Know?</h2>
                <p className="text-white/80 text-sm">Tap below to discover a new fact</p>
              </div>

              {/* Back Face */}
              <div
                className="absolute w-full h-full bg-white/90 backdrop-blur-xl rounded-3xl shadow-2xl group-hover:shadow-indigo-500/25 border border-slate-200/50 flex items-center justify-center p-12 transition-all duration-300"
                style={{
                  backfaceVisibility: "hidden",
                  transform: "rotateY(180deg)",
                }}
              >
                <div className="w-full h-full flex flex-col items-center justify-center gap-2">
                  <div className="bg-linear-to-br from-blue-100 to-indigo-200 p-3 rounded-2xl shadow-inner">
                    <PawPrint className="w-10 h-10 text-blue-600" />
                  </div>
                  <div className="w-full flex-1 flex items-center justify-center overflow-hidden">
                    <p className="text-lg md:text-xl font-semibold leading-relaxed text-center px-6 text-slate-800 max-w-md">
                      {fact || "Loading..."}
                    </p>
                  </div>
                  <div className="w-16 h-1 bg-linear-to-r from-blue-400 to-indigo-400 rounded-full"></div>
                </div>
              </div>
            </div>
          </div>

          <div className="flex flex-col items-center gap-4 animate-in fade-in slide-in-from-bottom duration-700 delay-300">
            <Button
              onClick={fetchFact}
              disabled={loading}
              size="lg"
              className="group text-lg px-10 py-6 rounded-full shadow-xl hover:shadow-2xl transition-all duration-300 hover:-translate-y-1 bg-linear-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-bold border-2 border-white/20 disabled:opacity-70 disabled:cursor-not-allowed"
            >
              <span className="flex items-center gap-3">
                {loading ? (
                  <>
                    <RefreshCw className="w-5 h-5 animate-spin" />
                    Fetching...
                  </>
                ) : (
                  <>
                    <RefreshCw className="w-5 h-5 group-hover:rotate-180 transition-transform duration-500" />
                    Discover New Fact
                  </>
                )}
              </span>
            </Button>
            <p className="text-sm text-slate-500 text-center flex items-center gap-2">
              <span className="inline-block w-2 h-2 bg-green-500 rounded-full animate-pulse"></span>
              Click to reveal dog trivia :D
            </p>
          </div>
        </div>
      </MainLayout>
    </>
  );
};

export default DogFacts;
