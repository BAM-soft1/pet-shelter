import { useLocation, useNavigate } from "react-router-dom";
import type { Animal } from "../../types/types";
import MainLayout from "@/components/layout/MainLayout";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import calculateAge from "@/utils/calculateAge";
import AdoptApplicationForm from "@/components/adopt-application-form";

export default function AnimalDetailPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const animal = location.state?.animal as Animal | null;

  if (!animal) {
    return (
      <MainLayout>
        <div className="p-6">No animal data found</div>
      </MainLayout>
    );
  }

  const age = calculateAge(animal.birthDate);

  return (
    <MainLayout>
      <div className="max-w-6xl mx-auto py-8 px-4">
        <div className="mb-6">
          <h1 className="text-3xl font-semibold">{animal.name}</h1>
          <p className="text-sm text-muted-foreground">{animal.breed?.name || animal.species.name} • {age} {age === 1 ? "year" : "years"} old</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Image card */}
          <Card className="h-full">
            <CardContent>
              <div className="relative h-96 overflow-hidden rounded-md bg-muted flex items-center justify-center">
                {animal.imageUrl ? (
                  // eslint-disable-next-line jsx-a11y/img-redundant-alt
                  <img src={animal.imageUrl} alt={animal.name} className="w-full h-full object-cover object-center" />
                ) : (
                  <div className="w-full h-full flex items-center justify-center bg-linear-to-br from-primary/10 to-primary/5">
                    <span className="text-6xl font-bold text-primary/60">{animal.species.name}</span>
                  </div>
                )}
              </div>
            </CardContent>
          </Card>

          {/* Info card */}
          <Card>
            <CardHeader>
              <CardTitle>{animal.name}</CardTitle>
              <CardDescription>{animal.species.name} — {animal.breed?.name || "Mixed breed"}</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-2 gap-3 mb-4">
                <div>
                  <p className="text-sm font-medium text-muted-foreground">Sex</p>
                  <Badge variant="secondary" className="capitalize mt-1">{animal.sex}</Badge>
                </div>
                <div>
                  <p className="text-sm font-medium text-muted-foreground">Status</p>
                  <Badge variant={animal.status === "AVAILABLE" ? "default" : "outline"} className={`capitalize mt-1 ${animal.status === "AVAILABLE" ? "bg-green-100 text-green-800 border-green-300" : ""}`}>
                    {animal.status}
                  </Badge>
                </div>
                <div>
                  <p className="text-sm font-medium text-muted-foreground">Birth Date</p>
                  <p className="text-sm mt-1">{new Date(animal.birthDate).toLocaleDateString()}</p>
                </div>
                <div>
                  <p className="text-sm font-medium text-muted-foreground">Intake Date</p>
                  <p className="text-sm mt-1">{new Date(animal.intakeDate).toLocaleDateString()}</p>
                </div>
              </div>

              <div className="mb-4">
                <p className="text-sm font-medium text-muted-foreground">Price</p>
                <p className="text-2xl font-bold text-primary mt-1">${animal.price}</p>
              </div>

              <div className="flex gap-2">
                <Button variant="outline" onClick={() => navigate(-1)}>Back</Button>
                <Button onClick={() => window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' })}>Apply Now</Button>
              </div>
            </CardContent>
          </Card>
        </div>

        <div className="mt-6">
          <Card>
            <CardHeader>
              <CardTitle>Adoption Application</CardTitle>
              <CardDescription>Fill out this short form to apply for {animal.name}.</CardDescription>
            </CardHeader>
            <AdoptApplicationForm animal={animal} onSubmit={(app) => { console.log('submitted app', app); }} />
          </Card>
        </div>
      </div>
    </MainLayout>
  );
}