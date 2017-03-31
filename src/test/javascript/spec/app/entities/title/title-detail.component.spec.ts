import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {TitleDetailComponent} from "../../../../../../main/webapp/app/entities/title/title-detail.component";
import {TitleService} from "../../../../../../main/webapp/app/entities/title/title.service";
import {Title} from "../../../../../../main/webapp/app/entities/title/title.model";

describe('Component Tests', () => {

	describe('Title Management Detail Component', () => {
		let comp: TitleDetailComponent;
		let fixture: ComponentFixture<TitleDetailComponent>;
		let service: TitleService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [TitleDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					TitleService
				]
			}).overrideComponent(TitleDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(TitleDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(TitleService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new Title(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.title).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
