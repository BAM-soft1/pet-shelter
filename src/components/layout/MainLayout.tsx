import { Header } from "./Header";
import { Footer } from "./Footer";

type MainLayoutProps = {
  children: React.ReactNode;
};
export default function MainLayout({ children }: MainLayoutProps) {
  return (
    <div className="min-h-screen flex flex-col bg-gray-50">
      <Header />
      <main className="grow container mx-auto px-6 py-8">{children}</main>
      <Footer />
    </div>
  );
}
